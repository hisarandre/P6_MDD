import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UpdateMeFormComponent} from "../../components/update-me-form/update-me-form.component";
import {
  SubjectsSubscribedListComponent
} from "../../../subjects/components/subjects-subscribed-list/subjects-subscribed-list.component";

@Component({
  selector: 'app-me-page',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    UpdateMeFormComponent,
    SubjectsSubscribedListComponent
  ],
  templateUrl: './me-page.component.html',
  styleUrl: './me-page.component.scss'
})
export class MePageComponent {

}
