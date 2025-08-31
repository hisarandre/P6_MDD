import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {SubjectWithStatus} from "../../../subjects/interfaces/subjectWithStatus.interface";
import {SubjectSubscribed} from "../../../subjects/interfaces/subjectSubscribed.interface";
import {Post} from "../../interfaces/post.interface";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-post-card',
  imports: [
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    DatePipe
  ],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.scss'
})
export class PostCardComponent {
  @Input() post!: Post;

}
