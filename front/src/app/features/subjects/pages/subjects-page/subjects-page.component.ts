import { Component } from '@angular/core';
import {SubjectsListComponent} from "../../components/subjects-list/subjects-list.component";

@Component({
  selector: 'app-subjects-page',
  standalone: true,
  imports: [SubjectsListComponent],
  templateUrl: './subjects-page.component.html',
  styleUrl: './subjects-page.component.scss'
})
export class SubjectsPageComponent {

}
