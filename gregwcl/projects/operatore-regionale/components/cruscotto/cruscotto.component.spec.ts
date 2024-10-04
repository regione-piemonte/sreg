import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CruscottoComponent } from './cruscotto.component';

describe('CruscottoComponent', () => {
  let component: CruscottoComponent;
  let fixture: ComponentFixture<CruscottoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CruscottoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CruscottoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
