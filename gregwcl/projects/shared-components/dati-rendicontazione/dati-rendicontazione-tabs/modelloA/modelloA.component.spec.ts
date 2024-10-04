import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelloAComponent } from './modelloA.component';

describe('ModelloAComponent', () => {
  let component: ModelloAComponent;
  let fixture: ComponentFixture<ModelloAComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelloAComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelloAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
