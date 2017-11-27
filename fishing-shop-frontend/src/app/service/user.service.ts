import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/user';

@Injectable()
export class UserService {
  host = 'http://localhost:8080/';
  private loggedInUser: User;

  constructor(protected http: HttpClient) {
    this.loggedInUser = null;
    const userString = localStorage.getItem('user');
    if (userString) {
      this.login(JSON.parse(userString)).subscribe(data => {
        this.loggedInUser = data;
      }, err => {
        console.log(err.error);
      });
    }
  }

  setLoggedInUser(user: User): void {
    this.loggedInUser = user;
    if (user) {
      localStorage.setItem('user', JSON.stringify(this.loggedInUser));
    }
  }

  getLoggedInUser(): User {
    return this.loggedInUser;
  }

  login(user: User) {
    return this.http.post<User>(this.host + 'api/user/login', user);
  }

  logout() {
    return this.http.post(this.host + 'api/user/logout', null);
  }

  register(user: User) {
    return this.http.post<User>(this.host + 'api/user/register', user);
  }

  deactivate() {
    return this.http.delete(this.host + 'api/user/deactivate');
  }

}
