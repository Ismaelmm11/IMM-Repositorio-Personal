import { Module } from '@nestjs/common';
import { DatabaseModule } from '../database/database.module';
import { UsersService } from './users.service';

@Module({
  imports: [DatabaseModule], // Importamos DatabaseModule para usar DatabaseService
  providers: [UsersService],
  exports: [UsersService], // Exportamos UsersService para usarlo en otros módulos
})
export class UsersModule {}