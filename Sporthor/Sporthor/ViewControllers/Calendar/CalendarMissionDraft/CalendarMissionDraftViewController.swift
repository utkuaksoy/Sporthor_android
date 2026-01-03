//
//  CalendarMissionDraftViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import UIKit

final class CalendarMissionDraftViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarMissionDraftPresenterProtocol {
        get { return self.basePresenter as! CalendarMissionDraftPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = UIEdgeInsets(top: 16, left: 0, bottom: 16, right: 0)
        return tableView
    }()
    
    // MARK: - Members
    
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
                .foregroundColor: DesignKitColorName.contentStrong900.color,
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
    
    // MARK: - Custom Methods
}

// MARK: - CalendarMissionDraftPresenterDelegate
extension CalendarMissionDraftViewController: CalendarMissionDraftPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

extension CalendarMissionDraftViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.drafts.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = MissionTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.bind(model: presenter.drafts[indexPath.row],
                  isDraft: true)
        return cell
    }
}

extension CalendarMissionDraftViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

extension CalendarMissionDraftViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
