// Importamos los decoradores y módulos necesarios de NestJS.
// Estos nos permiten definir el comportamiento de la clase y sus métodos.
import { Controller, Get, Post, Render, Res, Param, Body, Req, Query, Delete, ParseIntPipe } from '@nestjs/common';

// Importamos los servicios que este controlador utilizará para la lógica de negocio y acceso a datos.
// El controlador DELEGA estas tareas a los servicios.
import { AppService } from './app.service'; // Servicio general de la aplicación
import { UsersService, Servicio, Peluquero } from './users/users.service'; // Servicio específico para lógica relacionada con usuarios
import { TestService } from './test/test.service'; // Servicio específico para pruebas o lógica relacionada con 'test'

// Importamos los tipos de Request y Response del framework subyacente (generalmente Express, por defecto en NestJS).
// Los usamos cuando necesitamos acceso directo a los objetos de petición/respuesta HTTP.
import { Response, Request } from 'express';

// Decorador @Controller()
// Marca esta clase como un controlador NestJS.
// Un controlador es responsable de manejar las peticiones HTTP entrantes y devolver respuestas.
// Si no se especifica un argumento (como aquí), este controlador manejará rutas relativas a la raíz ('/').
@Controller()
export class AppController {
  // Constructor de la clase.
  // Aquí es donde se inyectan las dependencias (los servicios).
  // NestJS se encarga automáticamente de crear instancias de estos servicios y proporcionarlas al constructor.
  // Usamos 'private readonly' para declarar y asignar las propiedades de la clase al mismo tiempo,
  // y asegurarnos de que solo se puedan asignar en el constructor y no se modifiquen después.
  constructor(
    private readonly appService: AppService,
    private readonly testService: TestService,
    private readonly usersService: UsersService, // Usa UsersService
  ) {}

  @Get('/')
  @Render('main')
  default(@Req() req: Request) {
    return {
      title: 'Gentleman Barbershop',
      page: 'index',
      isAdmin: req.session.isAdmin || false,
    };
  }

  @Get('index')
  @Render('main')
  index(@Req() req: Request) {
    return {
      title: 'Gentleman Barbershop',
      page: 'index',
      isAdmin: req.session.isAdmin || false,
    };
  }

  @Get('prueba')
  @Render('main')
  prueba() {
    return {
      title: 'Prueba',
      page: 'prueba',
    };
  }

  @Get('prueba2')
  @Render('main')
  prueba2() {
    return {
      title: 'Prueba2',
      page: 'prueba2',
    };
  }

  @Get('test')
  @Render('main')
  async test() {
    return {
      title: 'Prueba de Base de Datos',
      page: 'test',
    };
  }

  @Get('login')
  @Render('main')
  login() {
    return {
      title: 'Iniciar Sesión - Gentleman Barbershop',
      page: 'login',
    };
  }

  @Get('gestion')
  @Render('main')
  gestion(@Req() req: Request, @Res() res: Response) {
    // Verificar si el usuario es admin
    if (!req.session.isAdmin) {
      return res.redirect('/login');
    }

    return {
      title: 'Panel de Gestión - Gentleman Barbershop',
      page: 'gestion',
      isAdmin: req.session.isAdmin || false,
    };
  }

  @Get('reserva')
  @Render('main')
  async reserva(@Req() req: Request): Promise<{ title: string; page: string; servicios: Servicio[]; peluqueros: Peluquero[]; isAdmin: boolean }> {
    const servicios = await this.usersService.getServicios();
    const peluqueros = await this.usersService.getPeluqueros();

    return {
      title: 'Reservar Cita - Gentleman Barbershop',
      page: 'reserva',
      servicios,
      peluqueros,
      isAdmin: req.session.isAdmin || false,
    };
  }
  
  @Get('horario')
  @Render('main')
  async mostrarHorario(@Query('fecha') fecha: string, @Req() req: Request) {
    const fechaSeleccionada = fecha ? new Date(fecha) : new Date();
    const fechaFormateada = fechaSeleccionada.toISOString().split('T')[0];

    return {
      title: 'Horario Laboral - Gentleman Barbershop',
      page: 'horario',
      fecha: fechaFormateada,
      isAdmin: req.session.isAdmin || false,
    };
  }

  @Get('horario/eventos-dia')
async obtenerEventos(@Query('fecha') fecha: string) {
  const fechaSeleccionada = fecha ? new Date(fecha) : new Date();
  const fechaFormateada = fechaSeleccionada.toISOString().split('T')[0];


  
  console.log(fecha);

  const citas = await this.usersService.getCitasPorFecha(fechaFormateada);

  const coloresPorServicio: { [key: string]: { backgroundColor: string; borderColor: string } } = {
    'Corte de pelo': { backgroundColor: '#90EE90', borderColor: '#008000' }, // Verde claro
    'Afeitado': { backgroundColor: '#ADD8E6', borderColor: '#4682B4' }, // Azul claro
    'Tinte': { backgroundColor: '#FFB6C1', borderColor: '#FF69B4' }, // Rosa claro
    'Sin servicio': { backgroundColor: '#D3D3D3', borderColor: '#A9A9A9' }, // Gris por defecto
  };

  // Convertimos a objetos con Date para facilitar la comparación
  const eventosCrudos = citas.map(cita => {
    const horaIniStr = cita.hora_ini.toString().slice(0, 5);
    const horaFinStr = cita.hora_fin.toString().slice(0, 5);

    const servicioNombre = cita.servicioNombre || 'Sin servicio';
    const colores = coloresPorServicio[servicioNombre] || coloresPorServicio['Sin servicio'];

    return {
      id: String(cita.id),
      title: `Cita - ${cita.clienteNombre || 'Sin cliente'}`,
      startStr: `${fechaFormateada}T${horaIniStr}:00`,
      endStr: `${fechaFormateada}T${horaFinStr}:00`,
      cliente: cita.clienteNombre || 'Sin cliente',
      servicio: cita.servicioNombre || 'Sin servicio',
      estado: cita.estado,
      horaIni: horaIniStr,
      horaFin: horaFinStr,
      backgroundColor: colores.backgroundColor,
      borderColor: colores.borderColor,
      textColor: '#000000',
      eventGroupId: 'citas'
    };
  });

  // Procesar solapamientos
  const eventosProcesados = eventosCrudos.map((evento, index, arr) => {
    const start = new Date(evento.startStr);
    const end = new Date(evento.endStr);
  
    // Buscar todos los eventos que se solapan con este
    const overlappingEvents = arr.filter((ev, i) => {
      if (i === index) return false;
      const s = new Date(ev.startStr);
      const e = new Date(ev.endStr);
      return s < end && start < e;
    });
  
    // El nivel actual es 0 si no hay solapamientos, o al menos 1 si hay
    const overlapLevel = overlappingEvents.length > 0 ? 1 : 0;
  
    // Ahora calculamos cuántos eventos se solapan entre sí (incluyendo el actual)
    const allInSameOverlapGroup = arr.filter(ev => {
      const s = new Date(ev.startStr);
      const e = new Date(ev.endStr);
      return s < end && start < e;
    });
  
    const maxOverlap = allInSameOverlapGroup.length;
  
    return {
      ...evento,
      start: evento.startStr,
      end: evento.endStr,
      overlapLevel,
      maxOverlap
    };
  });

  return eventosProcesados;
}

@Get('horario/eventos-semana')
async obtenerEventosSemana(@Query('fechaini') fechaini: string, @Query('fechafin') fechafin: string) {
  if (!fechaini || !fechafin) {
    throw new Error('Debe proporcionar un rango de fechas válido (fechaini y fechafin).');
  }

  console.log(fechaini + "y" + fechafin);

  const fechaIniFormateada = new Date(fechaini).toISOString().split('T')[0];
  const fechaFinFormateada = new Date(fechafin).toISOString().split('T')[0];

  const citas = await this.usersService.getCitasPorRangoFecha(fechaIniFormateada, fechaFinFormateada);

  const coloresPorServicio: { [key: string]: { backgroundColor: string; borderColor: string } } = {
    'Corte de pelo': { backgroundColor: '#90EE90', borderColor: '#008000' },
    'Afeitado': { backgroundColor: '#ADD8E6', borderColor: '#4682B4' },
    'Tinte': { backgroundColor: '#FFB6C1', borderColor: '#FF69B4' },
    'Sin servicio': { backgroundColor: '#D3D3D3', borderColor: '#A9A9A9' },
  };

  const eventosCrudos = citas.map(cita => {
    const horaIniStr = cita.hora_ini.toString().slice(0, 5);
    const horaFinStr = cita.hora_fin.toString().slice(0, 5);

    // ✅ CORREGIDO: Evitar desfase de día usando fecha local
    const citaFecha = new Date(cita.fecha).toLocaleDateString('en-CA');

    const servicioNombre = cita.servicioNombre || 'Sin servicio';
    const colores = coloresPorServicio[servicioNombre] || coloresPorServicio['Sin servicio'];

    return {
      id: String(cita.id),
      title: `Cita - ${cita.clienteNombre || 'Sin cliente'}`,
      startStr: `${citaFecha}T${horaIniStr}:00`,
      endStr: `${citaFecha}T${horaFinStr}:00`,
      cliente: cita.clienteNombre || 'Sin cliente',
      servicio: cita.servicioNombre || 'Sin servicio',
      estado: cita.estado,
      horaIni: horaIniStr,
      horaFin: horaFinStr,
      backgroundColor: colores.backgroundColor,
      borderColor: colores.borderColor,
      textColor: '#000000',
      eventGroupId: 'citas',
    };
  });

  const eventosProcesados = eventosCrudos.map((evento, index, arr) => {
    const start = new Date(evento.startStr);
    const end = new Date(evento.endStr);

    const overlappingEvents = arr.filter((ev, i) => {
      if (i === index) return false;
      const s = new Date(ev.startStr);
      const e = new Date(ev.endStr);
      return s < end && start < e;
    });

    const allInSameOverlapGroup = arr.filter(ev => {
      const s = new Date(ev.startStr);
      const e = new Date(ev.endStr);
      return s < end && start < e;
    });

    return {
      ...evento,
      start: evento.startStr,
      end: evento.endStr,
      overlapLevel: overlappingEvents.length > 0 ? 1 : 0,
      maxOverlap: allInSameOverlapGroup.length,
    };
  });

  console.log(eventosProcesados);

  return eventosProcesados;
}


@Get('horario/datos-formulario')
async obtenerDatosFormulario() {
  const clientes = await this.usersService.getClientes();
  const servicios = await this.usersService.getServiciosCita();

  return { clientes, servicios };
}

@Post('/api/editar-cita/:id')
async editarCita(@Param('id') id: number, @Body() body) {
  console.log('Editando cita con ID:', id, 'Datos:', body);
  const { cliente_id, servicio_id, hora_ini, hora_fin, fecha } = body;
  // Si es un descanso, cliente_id y servicio_id serán undefined o null
  await this.usersService.editarCita(id, cliente_id || null, servicio_id || null, fecha, hora_ini, hora_fin);
  return { ok: true };
}

@Post('/api/crear-cita')
async crearCita(@Body() body) {
  const { cliente_id, servicio_id, hora_ini, hora_fin, fecha } = body;
  const peluquero_id = 1; // Puedes mejorarlo con lógica real
  await this.usersService.createCitaPeluquero(cliente_id, servicio_id, peluquero_id, fecha, hora_ini, hora_fin);
  return { ok: true };
}

@Post('/api/crear-descanso')
async crearDescanso(@Body() body) {
  const { fecha, hora_ini, hora_fin } = body;
  await this.usersService.createCitaDescanso(fecha, hora_ini, hora_fin);
  return { ok: true };
}

@Delete('/api/borrar-cita/:id')
async borrarCita(@Param('id') id: number) {
  await this.usersService.borrarCita(id);
  return { ok: true };
}








  
  @Get('componente/:partialName')
  async getPartial(@Param('partialName') partialName: string, @Res() res: Response, @Req() req: Request) {
    if (partialName === 'test') {
      // Limpiar la base de datos (comentar esta línea cuando no quieras limpiar)
      //await this.testService.clearDatabase();

      const data = await this.testService.getAllData();

      console.log(data);

      return res.render(`componentes/${partialName}`, {
        title: 'Prueba de Base de Datos',
        ...data,
      });
    }
    
    return res.render(`componentes/${partialName}`, {
      title: partialName.charAt(0).toUpperCase() + partialName.slice(1),
      isAdmin: req.session.isAdmin || false, // Pasa isAdmin a todos los partials
    });
  }

// Métodos para la ruta POST
  // Maneja el envío del formulario de login. 
  // 'async' porque realiza una consulta a la base de datos (asíncrona).
  @Post('login')
  async loginPost(@Body() body: { user: string; pass: string }, @Res() res: Response, @Req() req: Request) {
    // @Body() body: Extrae el cuerpo de la petición (los datos del formulario, ej: { user: '...', pass: '...' }).
    // @Res() res: Response: Objeto de respuesta para manejo manual (usado aquí para redirigir).
    // @Req() req: Request: Objeto de petición para acceder a la sesión.

    // Desestructura el objeto 'body' para obtener 'user' y 'pass' en variables separadas.
    const { user, pass } = body;

    // Llama al método findUserByCredentials del usersService (operación asíncrona)
    // para buscar un usuario en la base de datos que coincida con las credenciales.
    // Esta es una mejora respecto a la versión anterior, delegando la lógica de base de datos al servicio.
    // NOTA IMPORTANTE DE SEGURIDAD: La función findUserByCredentials en UsersService debería
    // comparar la contraseña proporcionada con un HASH seguro de la contraseña almacenada,
    // NO comparar texto plano.
    const users = await this.usersService.findUserByCredentials(user, pass);

    // Verifica si la búsqueda no retornó resultados (!users o users.length === 0)
    // O si el usuario encontrado existe pero su propiedad 'tipo' no es 'admin'.
    if (!users || users.length === 0 || users[0].tipo !== 'admin') {
      // Si las credenciales son incorrectas o el usuario no es admin,
      // envía una respuesta de redirección HTTP al cliente para volver a la página de login.
      return res.redirect('/login');
    }

    // Si el login es exitoso y el usuario es admin, se ejecuta este bloque.
    // Asume que tienes configurado un middleware de sesión (ej: express-session).
    // Establece propiedades en el objeto de sesión para recordar el estado del usuario logeado.
    req.session.isAdmin = true; // Marca la sesión como la de un administrador
    req.session.userId = users[0].id; // Guarda el ID del usuario en la sesión

    // Envía una respuesta de redirección HTTP al cliente para llevarlo a la página principal.
    return res.redirect('/index');
  }

  @Post('gestion/crear-marca')
  async crearMarca(@Body() body: { nombre: string }, @Req() req: Request, @Res() res: Response) {
    // Verificar si el usuario es admin
    if (!req.session.isAdmin) {
      return res.redirect('/login');
    }

    const { nombre } = body;
    await this.usersService.createMarca(nombre);
    return res.redirect('/gestion');
  }

  @Post('gestion/crear-categoria')
  async crearCategoria(@Body() body: { nombre: string }, @Req() req: Request, @Res() res: Response) {
    // Verificar si el usuario es admin
    if (!req.session.isAdmin) {
      return res.redirect('/login');
    }

    const { nombre } = body;
    await this.usersService.createCategoria(nombre);
    return res.redirect('/gestion');
  }

  @Post('gestion/crear-servicio')
  async crearServicio(
    @Body() body: { nombre: string; descripcion: string; precio: string; duracion: string },
    @Req() req: Request,
    @Res() res: Response,
  ) {
    // Verificar si el usuario es admin
    if (!req.session.isAdmin) {
      return res.redirect('/login');
    }

    const { nombre, descripcion, precio, duracion } = body;
    await this.usersService.createServicio(nombre, descripcion, parseFloat(precio), parseInt(duracion, 10));
    return res.redirect('/gestion');
  }

  @Post('reserva/registrado')
  async reservarCitaRegistrado(
    @Body() body: { servicio_id: string; peluquero_id: string; fecha: string; hora: string; telefono: string },
    @Res() res: Response,
  ) {
    console.log('Solicitud POST /reserva/registrado recibida:', body);
    try {
      const { servicio_id, peluquero_id, fecha, hora, telefono } = body;

      const cliente = await this.usersService.findClienteByTelefono(telefono);
      if (!cliente) {
        const servicios = await this.usersService.getServicios();
        const peluqueros = await this.usersService.getPeluqueros();
        return res.status(400).render('main', {
          title: 'Reservar Cita - Gentleman Barbershop',
          page: 'reserva',
          servicios,
          peluqueros,
          error: 'Cliente no encontrado. Regístrate como nuevo cliente.',
        });
      }

      const fechaCita = new Date(fecha);
      const fechaActual = new Date();
      fechaActual.setHours(0, 0, 0, 0); // Ignorar la hora para comparar solo la fecha
      if (fechaCita < fechaActual) {
        const servicios = await this.usersService.getServicios();
        const peluqueros = await this.usersService.getPeluqueros();
        return res.status(400).render('main', {
          title: 'Reservar Cita - Gentleman Barbershop',
          page: 'reserva',
          servicios,
          peluqueros,
          error: 'No puedes reservar una cita con una fecha pasada.',
        });
      }

      await this.usersService.createCita(
        cliente.id,
        parseInt(servicio_id, 10),
        parseInt(peluquero_id, 10),
        fecha,
        hora,
      );

      return res.redirect('/index');
    } catch (err) {
      console.log('Error en POST /reserva/registrado:', err);
      const servicios = await this.usersService.getServicios();
      const peluqueros = await this.usersService.getPeluqueros();
      return res.status(500).render('main', {
        title: 'Reservar Cita - Gentleman Barbershop',
        page: 'reserva',
        servicios,
        peluqueros,
        error: 'Error al reservar la cita. Intenta de nuevo.',
      });
    }
  }

  @Post('reserva/nuevo')
  async reservarCitaNuevo(
    @Body() body: { servicio_id: string; peluquero_id: string; fecha: string; hora: string; nombre: string; apellidos: string; fecha_nac: string; telefono: string },
    @Res() res: Response,
  ) {
    console.log('Solicitud POST /reserva/nuevo recibida:', body);
    try {
      const { servicio_id, peluquero_id, fecha, hora, nombre, apellidos, fecha_nac, telefono } = body;

      const clienteId = await this.usersService.createCliente(nombre, apellidos, fecha_nac, telefono);

      const fechaCita = new Date(fecha);
      const fechaActual = new Date();
      fechaActual.setHours(0, 0, 0, 0); // Ignorar la hora para comparar solo la fecha
      if (fechaCita < fechaActual) {
        const servicios = await this.usersService.getServicios();
        const peluqueros = await this.usersService.getPeluqueros();
        return res.status(400).render('main', {
          title: 'Reservar Cita - Gentleman Barbershop',
          page: 'reserva',
          servicios,
          peluqueros,
          error: 'No puedes reservar una cita con una fecha pasada.',
        });
      }

      await this.usersService.createCita(
        clienteId,
        parseInt(servicio_id, 10),
        parseInt(peluquero_id, 10),
        fecha,
        hora,
      );

      return res.redirect('/index');
    } catch (err) {
      console.log('Error en POST /reserva/nuevo:', err);
      const servicios = await this.usersService.getServicios();
      const peluqueros = await this.usersService.getPeluqueros();
      return res.status(500).render('main', {
        title: 'Reservar Cita - Gentleman Barbershop',
        page: 'reserva',
        servicios,
        peluqueros,
        error: 'Error al crear el cliente o reservar la cita. Intenta de nuevo.',
      });
    }
  }
}