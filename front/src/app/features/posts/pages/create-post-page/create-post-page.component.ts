import { Component } from '@angular/core';
import {SubHeaderComponent} from "../../../../layout/components/sub-header/sub-header.component";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../auth/services/auth.service";
import {PostCreateFormComponent} from "../../components/post-create-form/post-create-form.component";

@Component({
  selector: 'app-create-post-page',
  standalone: true,
  imports: [
    SubHeaderComponent,
    PostCreateFormComponent
  ],
  templateUrl: './create-post-page.component.html',
  styleUrl: './create-post-page.component.scss'
})
export class CreatePostPageComponent {

  constructor(
    public readonly router: Router,
  ) {}

  public goBack(): void {
    this.router.navigate(['/posts']).catch(console.error);
  }
}
