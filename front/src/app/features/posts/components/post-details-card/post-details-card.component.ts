import {Component, Input} from '@angular/core';
import {DatePipe} from "@angular/common";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {Post} from "../../interfaces/post.interface";
import {PostDetails} from "../../interfaces/postDetails.interface";

@Component({
  selector: 'app-post-details-card',
    imports: [
        DatePipe,
    ],
  templateUrl: './post-details-card.component.html',
  styleUrl: './post-details-card.component.scss'
})
export class PostDetailsCardComponent {
  @Input() post!: PostDetails;
}
