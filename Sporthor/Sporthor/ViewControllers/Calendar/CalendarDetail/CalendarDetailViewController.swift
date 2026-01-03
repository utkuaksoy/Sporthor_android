//
//  CalendarDetailViewController.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import UIKit
import ComponentKit
import FSCalendar

final class CalendarDetailViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarDetailPresenterProtocol {
        get { return self.basePresenter as! CalendarDetailPresenterProtocol }
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
    
    private lazy var calendar: FSCalendar = {
        let calendar = FSCalendar()
        calendar.backgroundColor = .clear
        calendar.dataSource = self
        calendar.delegate = self
        calendar.scope = .week
        calendar.placeholderType = .none
        calendar.locale = Locale(identifier: "tr_TR")
        calendar.firstWeekday = 2
        calendar.appearance.caseOptions = [.weekdayUsesSingleUpperCase]
        calendar.layer.zPosition = 1
        
        // MARK: - Delete Calendar Header
        calendar.appearance.headerMinimumDissolvedAlpha = 0.0
        calendar.headerHeight = 0
        
        // MARK: - Weekday
        calendar.weekdayHeight = 0
        calendar.appearance.weekdayTextColor = .clear
        
        // MARK: - Day
        calendar.rowHeight = 80
        calendar.appearance.titleDefaultColor = .clear
        calendar.appearance.titlePlaceholderColor = .clear
        
        // MARK: - Today
        calendar.appearance.titleTodayColor = .clear
        calendar.appearance.subtitleTodayColor = .clear
        calendar.appearance.todayColor = .clear
        calendar.appearance.todaySelectionColor = .clear
        
        // MARK: - Selection Day
        calendar.appearance.selectionColor = .clear
        calendar.appearance.titleSelectionColor = .clear
        
        calendar.register(CustomCalendarCell.self, forCellReuseIdentifier: "CustomCalendarCell")
        
        return calendar
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 16, right: 0)
        tableView.separatorStyle = .none
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.backgroundColor = .clear
        tableView.layer.zPosition = 2
        return tableView
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
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            let appearance = UINavigationBarAppearance()
            appearance.configureWithTransparentBackground()
            appearance.backgroundColor = .clear
            appearance.shadowColor = .clear
            appearance.titleTextAttributes = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
            
            let navigationBar = navCon.navigationBar
            navigationBar.standardAppearance = appearance
            navigationBar.scrollEdgeAppearance = appearance
            navigationBar.compactAppearance = appearance
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
    }
    
    // MARK: - Custom Methods
}

// MARK: - CalendarDetailPresenterDelegate
extension CalendarDetailViewController: CalendarDetailPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackWhiteExist = true
        }
    }
    
    func prepareUI() {
        [topGradientBackgroundView,
         calendar,
         tableView,
         addButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview($0)
        }
        
        NSLayoutConstraint.activate([
            topGradientBackgroundView.topAnchor.constraint(equalTo: view.topAnchor),
            topGradientBackgroundView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            topGradientBackgroundView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            topGradientBackgroundView.heightAnchor.constraint(equalToConstant: 188),
            
            calendar.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            calendar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            calendar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            calendar.heightAnchor.constraint(equalToConstant: 400),
            
            tableView.topAnchor.constraint(equalTo: topGradientBackgroundView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            
            addButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            addButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            addButton.widthAnchor.constraint(equalToConstant: 64),
            addButton.heightAnchor.constraint(equalToConstant: 64)
        ])
        calendar.collectionView.setNeedsLayout()
        calendar.collectionView.layoutIfNeeded()
    }
    
    func setCalendarSelectedDate(_ date: Date) {
        calendar.select(date, scrollToDate: true)
        calendar.setCurrentPage(date, animated: true)
        calendar.reloadData()
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - CustomNavigationControllerDelegate
extension CalendarDetailViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}

// MARK: - FSCalendarDelegate
extension CalendarDetailViewController: FSCalendarDelegate {
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        presenter.didChangeDate(date)
        calendar.reloadData()
    }
    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        didSetTitle(calendar.currentPage.toString("MMMM"))
    }
}

// MARK: - FSCalendarDataSource
extension CalendarDetailViewController: FSCalendarDataSource {
    func calendar(_ calendar: FSCalendar, cellFor date: Date, at position: FSCalendarMonthPosition) -> FSCalendarCell {
        guard let cell = calendar.dequeueReusableCell(withIdentifier: "CustomCalendarCell", for: date, at: position) as? CustomCalendarCell else {
            return FSCalendarCell()
        }
        
        let isSelected = calendar.selectedDates.contains(date)
        cell.configure(with: date, isSelected: isSelected)
        return cell
    }
    
    func minimumDate(for calendar: FSCalendar) -> Date {
        return Date().addingTimeInterval(-60*60*24*365)
    }
    
    func maximumDate(for calendar: FSCalendar) -> Date {
        return Date().addingTimeInterval(60*60*24*365)
    }
}

extension CalendarDetailViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.tasks.isEmpty ? 1 : presenter.tasks.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if presenter.tasks.isEmpty {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(image: Asset.infoAlert.image, description: "Etkinliğiniz bulunmamaktadır.")
            return cell
        } else {
            let cell = MissionTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      model: presenter.tasks[indexPath.row])
            return cell
        }
    }
}

extension CalendarDetailViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

extension CalendarDetailViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension CalendarDetailViewController: MissionTableViewCellDelegate {
    func missionDetailRPEButtonTappedWithModel(_ model: GetCalendarDetailTaskModel) {
        presenter.didTappedRPEButton(model)
    }
    
    func missionDetailLocationTappedWithModel(_ model: GetCalendarDetailTaskModel) {
        presenter.didTappedLocationButton(model)
    }
}
