import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChiudiEnteComponent } from './chiudi-ente.component';

describe('ChiudiEnteComponent', () => {
  let component: ChiudiEnteComponent;
  let fixture: ComponentFixture<ChiudiEnteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChiudiEnteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChiudiEnteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
