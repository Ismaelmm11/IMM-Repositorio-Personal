import { Injectable } from '@nestjs/common';
import { DatabaseService } from '../database/database.service';
import { RowDataPacket, OkPacket } from 'mysql2/promise';



interface UserWithProfile {
  id: number;
  nombre: string;
  apellidos: string;
  fecha_nac: string;
  edad: number;
  telefono: string;
  num_cortes: number;
  user: string | null;
  pass: string | null;
  tipo: string;
}

export interface Servicio {
  id: number;
  nombre: string;
}

export interface Peluquero {
  id: number;
  nombre: string;
  apellidos: string;
}

export interface Cliente {
  id: number;
  nombre: string;
  apellidos: string;
}

@Injectable()
export class UsersService {
  constructor(private readonly databaseService: DatabaseService) {}

  async findUserByCredentials(user: string, pass: string): Promise<UserWithProfile[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT u.*, p.tipo FROM Usuario u JOIN Perfil p ON u.id = p.usuario_id WHERE u.user = ? AND u.pass = ?',
      [user, pass],
    );
    return result as UserWithProfile[];
  }

  async createMarca(nombre: string): Promise<void> {
    await this.databaseService.query(
      'INSERT INTO Marca (nombre) VALUES (?)',
      [nombre],
    );
  }

  async createCategoria(nombre: string): Promise<void> {
    await this.databaseService.query(
      'INSERT INTO Categoria (nombre) VALUES (?)',
      [nombre],
    );
  }

  async createServicio(nombre: string, descripcion: string, precio: number, duracion: number): Promise<void> {
    await this.databaseService.query(
      'INSERT INTO Servicio (nombre, descripcion, precio, duracion) VALUES (?, ?, ?, ?)',
      [nombre, descripcion, precio, duracion],
    );
  }

  async getServicios(): Promise<Servicio[]> {
    const result = await this.databaseService.query<RowDataPacket[]>('SELECT id, nombre FROM Servicio');
    console.log('Servicios:', result); // Depuración
    return result as Servicio[];
  }
  
  async getPeluqueros(): Promise<Peluquero[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT u.id, u.nombre, u.apellidos FROM Usuario u JOIN Perfil p ON u.id = p.usuario_id WHERE p.tipo = "peluquero"',
    );
    console.log('Peluqueros:', result); // Depuración
    return result as Peluquero[];
  }

  async findClienteByTelefono(telefono: string): Promise<UserWithProfile | null> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT u.* FROM Usuario u WHERE u.telefono = ?',
      [telefono],
    );
    const users = result as UserWithProfile[];
    return users.length > 0 ? users[0] : null;
  }

  async createCliente(nombre: string, apellidos: string, fecha_nac: string, telefono: string): Promise<number> {
    const result = await this.databaseService.query<OkPacket>(
      'INSERT INTO Usuario (nombre, apellidos, fecha_nac, telefono, num_cortes) VALUES (?, ?, ?, ?, 0)',
      [nombre, apellidos, fecha_nac, telefono],
    );
    return result.insertId;
  }

  async getDuracionServicio(servicio_id: number): Promise<number> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT duracion FROM Servicio WHERE id = ?',
      [servicio_id],
    );
    return result.length > 0 ? result[0].duracion : 0; // Duración en minutos
  }

  async getPrecioServicio(servicio_id: number): Promise<number> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT precio FROM Servicio WHERE id = ?',
      [servicio_id],
    );
    return result.length > 0 ? result[0].precio : 0;
  }
  
  async getPrecioProductosCita(cita_id: number): Promise<number> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT SUM(p.precio) as total FROM reserva_producto_cita rpc JOIN Producto p ON rpc.producto_id = p.id WHERE rpc.cita_id = ?',
      [cita_id],
    );
    return result.length > 0 ? result[0].total || 0 : 0;
  }

  async createCita(cliente_id: number, servicio_id: number, peluquero_id: number, fecha: string, hora: string): Promise<void> {
    // Obtener el precio y duración del servicio
    const precioServicio = await this.getPrecioServicio(servicio_id);
    const duracion = await this.getDuracionServicio(servicio_id);
  
    // Calcular hora_fin sumando la duración a hora_ini
    const [horas, minutos] = hora.split(':').map(Number); // hora es "HH:MM"
    const horaIniDate = new Date();
    horaIniDate.setHours(horas, minutos, 0, 0);
  
    const horaFinDate = new Date(horaIniDate);
    horaFinDate.setMinutes(horaIniDate.getMinutes() + duracion);
  
    const horaFin = `${horaFinDate.getHours().toString().padStart(2, '0')}:${horaFinDate.getMinutes().toString().padStart(2, '0')}`;
  
    // Insertar la cita con hora_ini y hora_fin
    const result = await this.databaseService.query<OkPacket>(
      'INSERT INTO Cita (cliente_id, servicio_id, peluquero_id, fecha, hora_ini, hora_fin, precio_final) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [cliente_id, servicio_id, peluquero_id, fecha, hora, horaFin, precioServicio],
    );
  
    // Obtener el ID de la cita recién creada
    const cita_id = result.insertId;
  
    // Calcular el precio de los productos asociados (si los hay)
    const precioProductos = await this.getPrecioProductosCita(cita_id);
  
    // Actualizar el precio_final con la suma del servicio y los productos
    const precioFinal = precioServicio + precioProductos;
    await this.databaseService.query(
      'UPDATE Cita SET precio_final = ? WHERE id = ?',
      [precioFinal, cita_id],
    );
  }

  async createCitaPeluquero(cliente_id: number, servicio_id: number, peluquero_id: number, fecha: string, hora_ini: string, hora_fin: string): Promise<void> {
    // Obtener el precio y duración del servicio
    const precioServicio = await this.getPrecioServicio(servicio_id);
  
    // Insertar la cita con hora_ini y hora_fin
    const result = await this.databaseService.query<OkPacket>(
      'INSERT INTO Cita (cliente_id, servicio_id, peluquero_id, fecha, hora_ini, hora_fin, precio_final) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [cliente_id, servicio_id, peluquero_id, fecha, hora_ini, hora_fin, precioServicio],
    );
  }

  async createCitaDescanso(fecha: string, hora_ini: string, hora_fin: string): Promise<void> {
  
    // Insertar la cita con hora_ini y hora_fin
    const result = await this.databaseService.query<OkPacket>(
      'INSERT INTO Cita (fecha, hora_ini, hora_fin, estado) VALUES (?, ?, ?, ?)',
      [fecha, hora_ini, hora_fin, 'descanso'],
    );
  }

  async getCitasPorFecha(fecha: string): Promise<any[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      `SELECT c.*, u.nombre AS clienteNombre, s.nombre AS servicioNombre
       FROM Cita c
       LEFT JOIN Usuario u ON c.cliente_id = u.id
       LEFT JOIN Servicio s ON c.servicio_id = s.id
       WHERE c.fecha = ?
       ORDER BY c.hora_ini ASC`, // <-- ¡Añadimos esta línea!
      [fecha],
    );
    // El resultado de result es típicamente un array [rows, fields],
    // solo queremos el array de filas (citas)
    return result;
  }

  async getCitasPorRangoFecha(fechaini: string, fechafin: string): Promise<any[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      `SELECT c.*, u.nombre AS clienteNombre, s.nombre AS servicioNombre
       FROM Cita c
       LEFT JOIN Usuario u ON c.cliente_id = u.id
       LEFT JOIN Servicio s ON c.servicio_id = s.id
       WHERE c.fecha BETWEEN ? AND ?
       ORDER BY c.fecha ASC, c.hora_ini ASC`,
      [fechaini, fechafin],
    );
    return result;
  }
  

  async getClientes(): Promise<Peluquero[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT u.id, u.nombre, u.apellidos FROM Usuario u JOIN Perfil p ON u.id = p.usuario_id WHERE p.tipo = "cliente"',
    );
    return result as Cliente[];
  }
  
  async getServiciosCita(): Promise<any[]> {
    const result = await this.databaseService.query<RowDataPacket[]>(
      'SELECT id, nombre FROM Servicio'
    );
    return result;
  }

  async editarCita(id: number, cliente_id: number, servicio_id: number, fecha: string, hora_ini: string, hora_fin: string): Promise<void> {
    const precio = await this.getPrecioServicio(servicio_id);
    await this.databaseService.query(
      `UPDATE Cita SET cliente_id = ?, servicio_id = ?, fecha = ?, hora_ini = ?, hora_fin = ?, precio_final = ? WHERE id = ?`,
      [cliente_id, servicio_id, fecha, hora_ini, hora_fin, precio, id]
    );
  }

  async borrarCita(id: number): Promise<void> {
    const result = await this.databaseService.query(
      'DELETE FROM Cita WHERE id = ?',
      [id]
    );
  }
}