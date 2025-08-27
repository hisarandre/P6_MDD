import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import { Component, EventEmitter, Output, Input } from '@angular/core';

@Component({
  selector: 'app-sub-header',
  standalone: true,
    imports: [
        MatIcon,
        MatIconButton
    ],
  templateUrl: './sub-header.component.html',
  styleUrl: './sub-header.component.scss'
})
export class SubHeaderComponent {
  @Input() title: string = '';
  @Input() titleAlign: 'left' | 'center' = 'center';
  @Output() back = new EventEmitter<void>();

}
