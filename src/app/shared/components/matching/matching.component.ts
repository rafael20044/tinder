import { Component, OnInit } from '@angular/core';
import Matching from 'src/plugins/matching';

@Component({
  selector: 'app-matching',
  templateUrl: './matching.component.html',
  styleUrls: ['./matching.component.scss'],
  standalone: false,
})
export class MatchingComponent  implements OnInit {

  constructor() { }

  async ngOnInit() {
    await Matching.open();
  }

}
