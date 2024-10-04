import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MacroaggregatiComponent } from './macroaggregati.component';

describe('MacroaggregatiComponent', () => {
  let component: MacroaggregatiComponent;
  let fixture: ComponentFixture<MacroaggregatiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MacroaggregatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MacroaggregatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
