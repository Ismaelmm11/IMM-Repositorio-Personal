import { Module } from '@nestjs/common';
import { DatabaseModule } from '../database/database.module';
import { TestService } from './test.service';

@Module({
  imports: [DatabaseModule],
  providers: [TestService],
  exports: [TestService],
})
export class TestModule {}