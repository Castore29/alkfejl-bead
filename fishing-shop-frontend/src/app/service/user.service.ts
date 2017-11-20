import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {User} from '../model/user';

@Injectable()
export class UserService {
  host = 'http://localhost:8080/';
  private loggedInUser: User;

  constructor(protected http: HttpClient) {
    this.loggedInUser = JSON.parse(localStorage.getItem('user'));
  }

  setLoggedInUser(user: User): void {
    this.loggedInUser = user;
    localStorage.setItem('user', JSON.stringify(this.loggedInUser));
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

  deregister() {
    return this.http.delete(this.host + 'api/user/deregister');
  }

}
