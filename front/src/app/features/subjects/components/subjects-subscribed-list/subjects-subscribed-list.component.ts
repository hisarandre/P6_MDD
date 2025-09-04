import { Component, OnDestroy, OnInit } from '@angular/core';
import { AsyncPipe } from "@angular/common";
import { SubjectCardComponent } from "../subject-card/subject-card.component";
import { BehaviorSubject, Subject, takeUntil, catchError, of } from "rxjs";
import { SubjectsService } from "../../services/subjects.service";
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
  private subjectsSubject = new BehaviorSubject<SubjectSubscribed[]>([]);
  allSubscribedSubjects$ = this.subjectsSubject.asObservable();

  private destroy$ = new Subject<void>();

  constructor(
    private readonly subjectsService: SubjectsService,
  ) { }

  ngOnInit(): void {
    this.subjectsService.getSubscribedSubjects()
      .pipe(
        catchError(() => of([])),
        takeUntil(this.destroy$)
      )
      .subscribe(subjects => this.subjectsSubject.next(subjects));
  }

  unsubscribe(subjectId: number): void {
    this.subjectsService.unsubscribe(subjectId)
      .pipe(
        catchError(() => of(null)),
        takeUntil(this.destroy$)
      )
      .subscribe((updatedSubjects: SubjectSubscribed[] | null) => {
        if (updatedSubjects) {
          this.subjectsSubject.next(updatedSubjects);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.subjectsSubject.complete();
  }
}
