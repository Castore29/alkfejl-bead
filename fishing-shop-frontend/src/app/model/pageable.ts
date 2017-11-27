import {BaseEntity} from './base-entity';

export class Pageable {
  content: BaseEntity[];
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  sort: {
    direction: string,
    property: string,
    ignoreCase: boolean,
    nullHandling: string,
    ascending: boolean,
  };
  totalElements: number;
  totalPages: number;
}
