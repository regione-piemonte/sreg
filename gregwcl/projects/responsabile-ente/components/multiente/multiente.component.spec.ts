import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultienteComponent } from './multiente.component';

describe('MultienteComponent', () => {
  let component: MultienteComponent;
  let fixture: ComponentFixture<MultienteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultienteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
