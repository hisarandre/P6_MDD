import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import {MatButton} from "@angular/material/button";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
  imports: [RouterLink, MatButton],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
