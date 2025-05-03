import { Injectable } from '@nestjs/common';
import { DatabaseService } from '../database/database.service';

@Injectable()
export class TestService {
  constructor(private readonly databaseService: DatabaseService) {}

  async clearDatabase(): Promise<void> {
    // Desactivar verificaciones de claves foráneas
    await this.databaseService.query('SET FOREIGN_KEY_CHECKS = 0');

    // Truncar todas las tablas (incluyendo DIA)
    await this.databaseService.query('TRUNCATE TABLE Producto_Pedido');
    await this.databaseService.query('TRUNCATE TABLE Pedido');
    await this.databaseService.query('TRUNCATE TABLE Reserva_Producto_Cita');
    await this.databaseService.query('TRUNCATE TABLE Cita');
    await this.databaseService.query('TRUNCATE TABLE Servicio');
    await this.databaseService.query('TRUNCATE TABLE Perfil');
    await this.databaseService.query('TRUNCATE TABLE Usuario');
    await this.databaseService.query('TRUNCATE TABLE Multimedia_Producto');
    await this.databaseService.query('TRUNCATE TABLE Producto');
    await this.databaseService.query('TRUNCATE TABLE Categoria');
    await this.databaseService.query('TRUNCATE TABLE Marca');
    await this.databaseService.query('TRUNCATE TABLE DIA');

    // Reactivar verificaciones de claves foráneas
    await this.databaseService.query('SET FOREIGN_KEY_CHECKS = 1');
  }

  async getAllData(): Promise<any> {
    const marca = await this.databaseService.query('SELECT * FROM Marca');
    const categoria = await this.databaseService.query('SELECT * FROM Categoria');
    const producto = await this.databaseService.query('SELECT * FROM Producto');
    const multimediaProducto = await this.databaseService.query('SELECT * FROM Multimedia_Producto');
    const usuario = await this.databaseService.query('SELECT * FROM Usuario');
    const perfil = await this.databaseService.query('SELECT * FROM Perfil');
    const servicio = await this.databaseService.query('SELECT * FROM Servicio');
    const cita = await this.databaseService.query('SELECT * FROM Cita');
    const reservaProductoCita = await this.databaseService.query('SELECT * FROM Reserva_Producto_Cita');
    const pedido = await this.databaseService.query('SELECT * FROM Pedido');
    const productoPedido = await this.databaseService.query('SELECT * FROM Producto_Pedido');

    return {
      marca,
      categoria,
      producto,
      multimediaProducto,
      usuario,
      perfil,
      servicio,
      cita,
      reservaProductoCita,
      pedido,
      productoPedido,
    };
  }
}