import { Component, OnInit } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {MatButton} from "@angular/material/button";
import {SubjectsService} from "../../../subjects/services/subjects.service";
import {SessionService} from "../../../../core/services/session.service";
import {catchError, of} from "rxjs";
import {AuthService} from "../../services/auth.service";

@Component({
    selector: 'app-home-page',
    templateUrl: './home-page.component.html',
    styleUrls: ['./home-page.component.scss'],
    standalone: true,
  imports: [RouterLink, MatButton],
})
export class HomePageComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
