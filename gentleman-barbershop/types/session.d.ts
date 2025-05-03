import { SessionData } from 'express-session';

declare module 'express-session' {
  interface SessionData {
    isAdmin?: boolean; // Propiedad para indicar si el usuario es admin
    userId?: number; // Propiedad para almacenar el ID del usuario
  }
}