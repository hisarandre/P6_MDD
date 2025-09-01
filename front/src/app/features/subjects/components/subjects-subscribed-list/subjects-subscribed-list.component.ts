import { Component, OnDestroy, OnInit } from '@angular/core';
import { AsyncPipe, NgForOf, NgIf } from "@angular/common";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { SubjectCardComponent } from "../subject-card/subject-card.component";
import { Observable, of, Subject, switchMap, takeUntil } from "rxjs";
import { SubjectsService } from "../../services/subjects.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { SubjectWithStatus } from "../../interfaces/subjectWithStatus.interface";
import { SubjectSubscribed } from "../../interfaces/subjectSubscribed.interface";

@Component({
  selector: 'app-subjects-subscribed-list',
  standalone: true,
  imports: [
    AsyncPipe,
    SubjectCardComponent
  ],
  templateUrl: './subjects-subscribed-list.component.html',
  styleUrl: './subjects-subscribed-list.component.scss'
})
export class SubjectsSubscribedListComponent implements OnInit, OnDestroy {
  allSubscribedSubjects$: Observable<SubjectSubscribed[]> = of();
  private destroy$ = new Subject<void>();

  constructor(
    private readonly subjectsService: SubjectsService,
  ) { }

  ngOnInit(): void {
    this.allSubscribedSubjects$ = this.subjectsService.getSubscribedSubjects();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  unsubscribe(subjectId: number): void {
    this.subjectsService.unsubscribe(subjectId).pipe(
      switchMap(() => this.subjectsService.getSubscribedSubjects()),
      takeUntil(this.destroy$)
    ).subscribe({
      next: (subjects) => {
        this.allSubscribedSubjects$ = of(subjects);
      },
      error: () => {
      }
    });
  }
}
