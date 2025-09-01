import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {PostsListComponent} from "../../components/posts-list/posts-list.component";
import {MatIcon} from "@angular/material/icon";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-posts-feed-page',
  standalone: true,
  imports: [
    MatButton,
    PostsListComponent,
    MatIcon,
    RouterLink,
  ],
  templateUrl: './posts-feed-page.component.html',
  styleUrl: './posts-feed-page.component.scss'
})
export class PostsFeedPageComponent {
  sortAscending = false;

  toggleSort(): void {
    this.sortAscending = !this.sortAscending;
  }
}
