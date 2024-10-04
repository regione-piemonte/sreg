import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CronologiaModelliComponent } from './cronologia-modelli.component';

describe('CronologiaModelliComponent', () => {
  let component: CronologiaModelliComponent;
  let fixture: ComponentFixture<CronologiaModelliComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CronologiaModelliComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CronologiaModelliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
