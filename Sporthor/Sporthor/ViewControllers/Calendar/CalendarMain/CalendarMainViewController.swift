//
//  CalendarMainViewController.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import UIKit
import ComponentKit
import FSCalendar
import BarVisibilityKit

final class CalendarMainViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: CalendarMainPresenterProtocol {
        get { return self.basePresenter as! CalendarMainPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var topGradientBackgroundView: CKGradientView = {
        let gradientView = CKGradientView(
            colors: [
                DesignKitColorName.backgroundPrimaryGreen.color.withAlphaComponent(0.15),
                .clear
            ],
            startPoint: CGPoint(x: 0.0, y: 0.5),
            endPoint: CGPoint(x: 0.3, y: 0.8)
        )
        gradientView.backgroundColor = DesignKitColorName.contentStrong900.color
        return gradientView
    }()
    
    
    private lazy var addButton: CKButton = {
        let button = CKButton(delegate: self,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 32,
                              image: Asset.blackPlus.image,
                              tag: 1)
        button.layer.zPosition = 99
        return button
    }()
    
    private lazy var headerDateView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }()
    
    private lazy var leftArrowButtonHeaderDateView: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.chevronLeftWhiteIcon.image,
                              tag: 2)
        return button
    }()
    
    private lazy var monthLabelHeaderDateView: CKLabel = {
        let label = CKLabel(textColor: .white,
                            textAlignment: .center,
                            font: .heading01)
        return label
    }()
    
    private lazy var rightArrowButtonHeaderDateView: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.chevronRightWhiteIcon.image,
                              tag: 3)
        return button
    }()
    
    private lazy var calendar: FSCalendar = {
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
        calendar.appearance.weekdayTextColor = .white
        calendar.appearance.weekdayFont = .bold06Compact
        
        // MARK: - Day
        calendar.rowHeight = 48
        calendar.appearance.titleFont = .heading06
        calendar.appearance.titleDefaultColor = .black
        
        // MARK: - Selection Day
        calendar.appearance.selectionColor = DesignKitColorName.backgroundPrimaryGreen.color
        calendar.appearance.titleSelectionColor = .black
        
        // MARK: - Today
        calendar.appearance.todayColor = .black
        calendar.appearance.titleTodayColor = .white
        return calendar
    }()
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftWhiteIcon.image
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    // MARK: - Members
    private var isNavigationAndTabbarHidden: Bool {
        return navigationController?.viewControllers.count == 1
    }
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupNavigationBar()
    }
    
    // MARK: - Custom Methods
    @objc private func didTappedBackButton() {
        presenter.didTappedNavigationButton(.back)
    }
    
    func setupNavigationBar() {
        configureNavigationBar()
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()
        appearance.backgroundColor = .clear
        appearance.shadowColor = .clear
        appearance.titleTextAttributes = [
            .foregroundColor: UIColor.white,
            .font: UIFont.bold03Compact
        ]
        
        let navigationBar = navigationController?.navigationBar
        navigationBar?.standardAppearance = appearance
        navigationBar?.scrollEdgeAppearance = appearance
        navigationBar?.compactAppearance = appearance
        
        navigationBar?.isTranslucent = true
        navigationBar?.backgroundColor = .clear

        navigationItem.leftBarButtonItem = backButton
    }
}

// MARK: - CalendarMainPresenterDelegate
extension CalendarMainViewController: CalendarMainPresenterDelegate {
    func prepareUI() {
        [leftArrowButtonHeaderDateView,
         monthLabelHeaderDateView,
         rightArrowButtonHeaderDateView].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            headerDateView.addSubview($0)
        }
        
        NSLayoutConstraint.activate([
            leftArrowButtonHeaderDateView.topAnchor.constraint(equalTo: headerDateView.topAnchor),
            leftArrowButtonHeaderDateView.leadingAnchor.constraint(equalTo: headerDateView.leadingAnchor),
            leftArrowButtonHeaderDateView.bottomAnchor.constraint(equalTo: headerDateView.bottomAnchor),
            leftArrowButtonHeaderDateView.widthAnchor.constraint(equalToConstant: 40),
            
            monthLabelHeaderDateView.topAnchor.constraint(equalTo: headerDateView.topAnchor),
            monthLabelHeaderDateView.leadingAnchor.constraint(equalTo: leftArrowButtonHeaderDateView.trailingAnchor, constant: 8),
            monthLabelHeaderDateView.bottomAnchor.constraint(equalTo: headerDateView.bottomAnchor),
            
            rightArrowButtonHeaderDateView.topAnchor.constraint(equalTo: headerDateView.topAnchor),
            rightArrowButtonHeaderDateView.leadingAnchor.constraint(equalTo: monthLabelHeaderDateView.trailingAnchor, constant: 8),
            rightArrowButtonHeaderDateView.trailingAnchor.constraint(equalTo: headerDateView.trailingAnchor),
            rightArrowButtonHeaderDateView.bottomAnchor.constraint(equalTo: headerDateView.bottomAnchor),
            rightArrowButtonHeaderDateView.widthAnchor.constraint(equalToConstant: 40),
        ])
        
        [topGradientBackgroundView,
         headerDateView,
         calendar,
         addButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview($0)
        }
        
        var height: CGFloat = 188
        if UIScreen.main.bounds.height < 668 {
            height = 150
        }
        
        NSLayoutConstraint.activate([
            topGradientBackgroundView.topAnchor.constraint(equalTo: view.topAnchor),
            topGradientBackgroundView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            topGradientBackgroundView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            topGradientBackgroundView.heightAnchor.constraint(equalToConstant: height),
            
            headerDateView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            headerDateView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 8),
            headerDateView.heightAnchor.constraint(equalToConstant: 40),
            
            calendar.topAnchor.constraint(equalTo: headerDateView.bottomAnchor),
            calendar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            calendar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            calendar.heightAnchor.constraint(equalToConstant: 400),
            
            addButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            addButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            addButton.widthAnchor.constraint(equalToConstant: 64),
            addButton.heightAnchor.constraint(equalToConstant: 64)
        ])
        
        presenter.moveCurrentPage(by: 0, currentPage: calendar.currentPage)
    }
    
    func setCurrentPage(newPage: Date) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            calendar.setCurrentPage(newPage, animated: true)
        }
    }
    
    func didChangeMonthName(_ text: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            monthLabelHeaderDateView.text = text
        }
    }
    
    func calendarReloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            calendar.reloadData()
        }
    }
}

// MARK: - CKButtonDelegate
extension CalendarMainViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 1: // Add Button
            presenter.didTappedAddMissionButton()
        case 2: // Left Arrow
            presenter.moveCurrentPage(by: -1,
                                      currentPage: calendar.currentPage)
        case 3: // Right Arrow
            presenter.moveCurrentPage(by: 1,
                                      currentPage: calendar.currentPage)
        default:
            break
        }
    }
}

// MARK: - CustomNavigationControllerDelegate
extension CalendarMainViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}

extension CalendarMainViewController: FSCalendarDelegate {
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        presenter.didSelectDate(date)
    }
    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        didChangeMonthName(calendar.currentPage.toString("MMMM"))
        presenter.calendarCurrentPageDidChange(calendar.currentPage)
    }
}

extension CalendarMainViewController: FSCalendarDataSource {
    func minimumDate(for calendar: FSCalendar) -> Date {
        return Date().addingTimeInterval(-60*60*24*365)
    }
    
    func maximumDate(for calendar: FSCalendar) -> Date {
        return Date().addingTimeInterval(60*60*24*365)
    }
    
    func calendar(_ calendar: FSCalendar, numberOfEventsFor date: Date) -> Int {
        let targetDate = Calendar.current.startOfDay(for: date)
        
        let totalCount = presenter.calendarEvents
            .filter {
                guard let eventDate = $0.date.toDate("dd.MM.yyyy") else { return false }
                return Calendar.current.isDate(eventDate, inSameDayAs: targetDate)
            }
            .flatMap { $0.taks }
            .count

        return totalCount
    }
}

extension CalendarMainViewController: FSCalendarDelegateAppearance {
    func calendar(_ calendar: FSCalendar, appearance: FSCalendarAppearance, eventDefaultColorsFor date: Date) -> [UIColor]? {
        let targetDate = Calendar.current.startOfDay(for: date)

        let colors = presenter.calendarEvents
            .filter {
                guard let eventDate = $0.date.toDate("dd.MM.yyyy") else { return false }
                return Calendar.current.isDate(eventDate, inSameDayAs: targetDate)
            }
            .flatMap { $0.taks }
            .compactMap { UIColor(hex: $0) }

        return colors.isEmpty ? nil : colors
    }
}
