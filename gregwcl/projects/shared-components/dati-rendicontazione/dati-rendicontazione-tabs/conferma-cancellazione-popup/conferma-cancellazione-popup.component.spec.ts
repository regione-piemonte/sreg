import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfermaCancellazionePopup } from './conferma-cancellazione-popup.component';

describe('ConfermaCancellazionePopup', () => {
  let component: ConfermaCancellazionePopup;
  let fixture: ComponentFixture<ConfermaCancellazionePopup>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfermaCancellazionePopup ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfermaCancellazionePopup);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
