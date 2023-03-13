import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { AcknowledgeInfo } from '../../client';

@Component({
  selector: 'app-advance-ack-help',
  templateUrl: './AdvanceAckHelp.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdvanceAckHelp {

  @Input()
  verifiers: AcknowledgeInfo[] = [];

  @Input()
  extra: AcknowledgeInfo[] = [];
}
