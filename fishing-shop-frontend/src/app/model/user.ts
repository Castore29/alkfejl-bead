import {BaseEntity} from './base-entity';

export interface User extends BaseEntity {
  userName?: string;
  email?: string;
  password?: string;
  address?: string;
  phoneNumber?: string;
  role?: Role;
}

export enum Role {
  ADMIN, USER, GUEST
}
