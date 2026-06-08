import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfesorLayout } from './profesor-layout';

describe('ProfesorLayout', () => {
  let component: ProfesorLayout;
  let fixture: ComponentFixture<ProfesorLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfesorLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfesorLayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
