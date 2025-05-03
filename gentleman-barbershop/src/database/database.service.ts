import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { createPool, Pool, RowDataPacket, OkPacket } from 'mysql2/promise';

@Injectable()
export class DatabaseService {
  private pool: Pool;

  constructor(configService: ConfigService) {
    this.pool = createPool({
      host: configService.get<string>('DATABASE_HOST', 'localhost'),
      port: configService.get<number>('DATABASE_PORT', 3306),
      user: configService.get<string>('DATABASE_USER', 'gentleman'),
      password: configService.get<string>('DATABASE_PASSWORD', 'V1rg1g14.1103'),
      database: configService.get<string>('DATABASE_NAME', 'BD_GENTLEMAN'),
    });
  }

  async query<T extends RowDataPacket[] | OkPacket>(sql: string, params: any[] = []): Promise<T> {
    const [result] = await this.pool.execute(sql, params);
    return result as T;
  }

  async close(): Promise<void> {
    await this.pool.end();
  }


  async onModuleInit() {
    try {
      const connection = await this.pool.getConnection();
      console.log('Conexión a MySQL establecida');
      connection.release();
    } catch (err) {
      console.error('Error al conectar a MySQL:', err);
      throw err;
    }
  }

  async onModuleDestroy() {
    await this.pool.end();
    console.log('Conexión a MySQL cerrada');
  }
}