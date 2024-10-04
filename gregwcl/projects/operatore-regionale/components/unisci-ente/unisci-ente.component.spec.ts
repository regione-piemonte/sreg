import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnisciEnteComponent } from './unisci-ente.component';

describe('UnisciEnteComponent', () => {
  let component: UnisciEnteComponent;
  let fixture: ComponentFixture<UnisciEnteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnisciEnteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnisciEnteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
