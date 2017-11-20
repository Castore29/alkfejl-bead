import {BaseEntity} from './base-entity';

export class User extends BaseEntity {
  userName: string;
  email: string;
  password: string;
  address: string;
  phoneNumber: string;
  role: string;
}
