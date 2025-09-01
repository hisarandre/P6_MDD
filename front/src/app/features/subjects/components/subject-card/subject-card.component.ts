import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SubjectWithStatus } from "../../interfaces/subjectWithStatus.interface";
import { SubjectSubscribed } from "../../interfaces/subjectSubscribed.interface";
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from "@angular/material/card";
import { MatButton } from "@angular/material/button";

@Component({
  selector: 'app-subject-card',
  standalone: true,
  imports: [
    MatCardHeader,
    MatCardTitle,
    MatCard,
    MatCardContent,
    MatCardActions,
    MatButton,
  ],
  templateUrl: './subject-card.component.html',
  styleUrl: './subject-card.component.scss'
})
export class SubjectCardComponent {
  @Input() subject!: SubjectWithStatus | SubjectSubscribed;
  @Input() showOnlySubscribed = false;
  @Output() onClickButton = new EventEmitter<number>();

  onClick(): void {
    this.onClickButton.emit(this.subject.id);
  }

  private hasSubscriptionStatus(subject: SubjectWithStatus | SubjectSubscribed): subject is SubjectWithStatus {
    return 'isSubscribed' in subject;
  }

  getButtonText(): string {
    if (this.hasSubscriptionStatus(this.subject)) {
      return this.subject.isSubscribed ? 'Déjà abonné' : "S'abonner";
    }

    return 'Se désabonner';
  }

  isButtonDisabled(): boolean {
    if (this.showOnlySubscribed) {
      return false;
    }

    if (this.hasSubscriptionStatus(this.subject)) {
      return this.subject.isSubscribed;
    }

    return false;
  }

}
