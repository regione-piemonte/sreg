import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RipristinaEnteComponent } from './ripristina-ente.component';

describe('RipristinaEnteComponent', () => {
  let component: RipristinaEnteComponent;
  let fixture: ComponentFixture<RipristinaEnteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RipristinaEnteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RipristinaEnteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
