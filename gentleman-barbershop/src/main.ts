import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { NestExpressApplication } from '@nestjs/platform-express';
import * as hbs from 'hbs';
import * as session from 'express-session'; // Importa express-session
import * as express from 'express';

async function bootstrap() {
  const app = await NestFactory.create<NestExpressApplication>(AppModule);

  app.setBaseViewsDir('views');
  app.setViewEngine('hbs');

  hbs.registerPartials('views/componentes');

  hbs.registerHelper('includePartial', function (pageName) {
    return pageName;
  });

  hbs.registerHelper('json', function (context) {
    return JSON.stringify(context);
  });

  app.use(express.json());
  app.use(express.urlencoded({ extended: true }));

  // Configurar sesiones
  app.use(
    session({
      secret: 'gentleman-barbershop-secret', // Clave secreta para firmar la sesión
      resave: false, // No guarda la sesión si  session si no hay cambios
      saveUninitialized: false, // No crea sesiones nuevas si no hay datos
      cookie: { maxAge: 24 * 60 * 60 * 1000 }, // Sesión dura 24 horas
    }),
  );

  app.useStaticAssets('public/CSS');
  app.useStaticAssets('public/JS');
  app.useStaticAssets('public/Multimedia/GIFs');
  app.useStaticAssets('public/Multimedia/Imagenes');
  app.useStaticAssets('public/Multimedia/Logos');

  await app.listen(3000);
  console.log(`🚀 Servidor corriendo en http://localhost:3000`);
}
bootstrap();