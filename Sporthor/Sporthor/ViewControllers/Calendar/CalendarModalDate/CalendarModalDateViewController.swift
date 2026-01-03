//
//  CalendarModalDateViewController.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import UIKit
import ComponentKit
import PanModal
import FSCalendar

final class CalendarModalDateViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarModalDatePresenterProtocol {
        get { return self.basePresenter as! CalendarModalDatePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        return view
    }()
    
    private lazy var dateStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 16)
        stackView.addArrangedSubviews([leftArrowButton,
                                       monthLabel,
                                       rightArrowButton])
        return stackView
    }()
    
    private lazy var leftArrowButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.chevronLeftIcon.image,
                              tag: 1)
        button.widthAnchor.constraint(equalToConstant: 24).isActive = true
        button.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return button
    }()
    
    private lazy var monthLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            textAlignment: .center,
                            font: .bold03Compact)
        return label
    }()
    
    private lazy var rightArrowButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.chevronRight.image,
                              tag: 2)
        button.widthAnchor.constraint(equalToConstant: 24).isActive = true
        button.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return button
    }()
    
    private lazy var calendarView: FSCalendar = {
        let calendar = FSCalendar()
        calendar.dataSource = self
        calendar.delegate = self
        calendar.translatesAutoresizingMaskIntoConstraints = false
        calendar.backgroundColor = .clear
        calendar.layer.zPosition = 2
        calendar.placeholderType = .none
        calendar.locale = Locale(identifier: "tr_TR")
        calendar.firstWeekday = 2
        calendar.appearance.caseOptions = [.weekdayUsesSingleUpperCase]
        
        // MARK: - Delete Calendar Header
        calendar.appearance.headerMinimumDissolvedAlpha = 0.0
        calendar.headerHeight = 0
        
        // MARK: - Weekday
        calendar.weekdayHeight = 48
        calendar.appearance.weekdayTextColor = DesignKitColorName.contentStrong900.color
        calendar.appearance.weekdayFont = .bold06Compact
        
        // MARK: - Day
        calendar.rowHeight = 48
        calendar.appearance.titleFont = .heading06
        calendar.appearance.titleDefaultColor = DesignKitColorName.contentStrong900.color
        
        // MARK: - Selection Day
        calendar.appearance.selectionColor = DesignKitColorName.backgroundPrimaryGreen.color
        calendar.appearance.titleSelectionColor = DesignKitColorName.contentStrong900.color
        
        // MARK: - Today
        calendar.appearance.todayColor = .clear
        calendar.appearance.titleTodayColor = DesignKitColorName.contentStrong900.color
        return calendar
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Onayla",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 3)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        let weekdayView = calendarView.calendarWeekdayView
        if weekdayView.viewWithTag(999) == nil {
            let line = UIView(frame: CGRect(x: 0, y: weekdayView.bounds.height - 1, width: weekdayView.bounds.width, height: 1))
            line.backgroundColor = DesignKitColorName.borderSoft200.color
            line.autoresizingMask = [.flexibleWidth, .flexibleTopMargin]
            line.tag = 999
            weekdayView.addSubview(line)
        }
    }
    
    // MARK: - Custom Methods
}

// MARK: - CalendarModalDatePresenterDelegate
extension CalendarModalDateViewController: CalendarModalDatePresenterDelegate {
    func prepareUI() {
        [scrollLineView,
         dateStackView,
         calendarView,
         submitButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview($0)
        }
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            dateStackView.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            dateStackView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            calendarView.topAnchor.constraint(equalTo: dateStackView.bottomAnchor, constant: 16),
            calendarView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            calendarView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            calendarView.bottomAnchor.constraint(equalTo: submitButton.topAnchor, constant: -16),
        
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
        ])
    }
    
    func setCurrentPage(newPage: Date) {
        let validDate = max(
            min(newPage, calendarView.maximumDate), calendarView.minimumDate
        )
//        calendarView.setCurrentPage(validDate, animated: true)
        calendarView.select(validDate, scrollToDate: true)
    }
    
    func didChangeMonthName(_ text: String) {
        monthLabel.text = text
    }
    
    func calendarReloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            calendarView.reloadData()
        }
    }
}

// MARK: - CKButtonDelegate
extension CalendarModalDateViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 1: // Left Arrow
            presenter.moveCurrentPage(by: -1,
                                      currentPage: calendarView.currentPage)
        case 2: // Right Arrow
            presenter.moveCurrentPage(by: 1,
                                      currentPage: calendarView.currentPage)
        case 3:
            presenter.didTappedSubmitButton()
        default:
            break
        }
    }
}

// MARK: - FSCalendarDelegate
extension CalendarModalDateViewController: FSCalendarDelegate {
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        presenter.didSelectDate(date)
    }
    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        didChangeMonthName(calendar.currentPage.toString("MMMM yyyy"))
    }
}

// MARK: - FSCalendarDataSource
extension CalendarModalDateViewController: FSCalendarDataSource {
    func minimumDate(for calendar: FSCalendar) -> Date {
        return Date()
    }
    
    func maximumDate(for calendar: FSCalendar) -> Date {
        return Date().addingTimeInterval(60*60*24*365)
    }
}

// MARK: - PanModalPresentable
extension CalendarModalDateViewController: PanModalPresentable {
    var allowsExtendedPanScrolling: Bool {
        return true
    }
    
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var longFormHeight: PanModalHeight {
        return .contentHeight(564)
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }
    
    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
}
