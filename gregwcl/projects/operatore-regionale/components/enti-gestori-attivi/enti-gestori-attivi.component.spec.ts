import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntiGestoriAttiviComponent } from './enti-gestori-attivi.component';

describe('EntiGestoriAttiviComponent', () => {
  let component: EntiGestoriAttiviComponent;
  let fixture: ComponentFixture<EntiGestoriAttiviComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntiGestoriAttiviComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntiGestoriAttiviComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
