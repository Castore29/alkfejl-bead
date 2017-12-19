import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  @Output() selectUser;

  constructor(private userService: UserService) {
    this.selectUser = new EventEmitter();
  }

  ngOnInit() {
    this.userService.getList().subscribe(result => {
      this.users = result.content;
    }, error => {
      console.log(error);
    });
  }

  select(user: User) {
    this.selectUser.emit(user);
  }

}
