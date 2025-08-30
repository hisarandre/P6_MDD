import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import {MatButton} from "@angular/material/button";

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
