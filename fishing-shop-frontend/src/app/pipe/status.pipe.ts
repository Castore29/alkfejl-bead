import {Pipe, PipeTransform} from '@angular/core';
import {Status} from '../models/order';

@Pipe({
  name: 'status'
})
export class StatusPipe implements PipeTransform {

  transform(value: Status): string {
    let result: string;
    const status = value.toString();
    switch (status) {
      case 'RECEIVED':
        result = 'Beérkezett';
        break;
      case 'PROCESSED':
        result = 'Feldolgozva';
        break;
      case 'DELIVERING':
        result = 'Szállítás alatt';
        break;
      case 'CLOSED':
        result = 'Lezárva';
        break;
      case 'CANCELLED':
        result = 'Törölve';
        break;
    }
    return result;
  }
}
