import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatoEnteComponent } from './stato-ente.component';

describe('StatoEnteComponent', () => {
  let component: StatoEnteComponent;
  let fixture: ComponentFixture<StatoEnteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatoEnteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatoEnteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
