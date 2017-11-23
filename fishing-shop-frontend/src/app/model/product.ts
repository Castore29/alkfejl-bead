import {BaseEntity} from './base-entity';

export interface Product extends BaseEntity {
  itemNumber?: string;
  category?: string;
  subCategory?: string;
  productName?: string;
  description?: string;
  producer?: string;
  image?: string;
  stock?: number;
  available?: boolean;
  price?: number;
  discount?: number;
}
