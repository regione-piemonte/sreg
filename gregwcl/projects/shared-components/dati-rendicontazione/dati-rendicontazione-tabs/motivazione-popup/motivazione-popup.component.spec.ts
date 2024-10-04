import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MotivazionePopupComponent } from './motivazione-popup.component';

describe('MotivazionePopupComponent', () => {
  let component: MotivazionePopupComponent;
  let fixture: ComponentFixture<MotivazionePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MotivazionePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MotivazionePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
