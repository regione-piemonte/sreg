import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuovaPrestazioneComponent } from './nuova-prestazione.component';

describe('NuovaPrestazioneComponent', () => {
  let component: NuovaPrestazioneComponent;
  let fixture: ComponentFixture<NuovaPrestazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuovaPrestazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuovaPrestazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
