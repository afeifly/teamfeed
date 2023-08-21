import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  userLoggedIn = new BehaviorSubject(false);

  constructor() { }
}
