import {BaseEntity} from './base-entity';
import {User} from './user';
import {Product} from './product';

export interface Order extends BaseEntity {
  orderNumber?: string;
  user?: User;
  total?: number;
  products?: Product[];
  status?: Status;
}

export enum Status {
  RECEIVED = 'RECEIVED',
  PROCESSED = 'PROCESSED',
  DELIVERING = 'DELIVERING',
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}
