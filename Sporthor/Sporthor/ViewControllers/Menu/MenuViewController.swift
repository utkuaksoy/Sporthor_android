//
//  MenuViewController.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import UIKit

final class MenuViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: MenuPresenterProtocol {
        get { return self.basePresenter as! MenuPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.contentInset = UIEdgeInsets(top: 16, left: 0, bottom: 16, right: 0)
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.allowsSelection = true
        tableView.separatorInset = UIEdgeInsets(top: 0, left: 24, bottom: 0, right: 24)
        tableView.separatorColor = DesignKitColorName.borderSoft200.color
        tableView.removeEmptyCell()
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
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - MenuPresenterDelegate
extension MenuViewController: MenuPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
            navCon.navigationBar.titleTextAttributes = [
                .foregroundColor: DesignKitColorName.contentStrong900.color,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

extension MenuViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.tempMenu.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = MenuTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.configure(delegate: self,
                       model: presenter.tempMenu[indexPath.row])
        return cell
    }
}

// MARK: - MenuTableViewCellDelegate
extension MenuViewController: MenuTableViewCellDelegate {
    func updates() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            tableView.beginUpdates()
            tableView.endUpdates()
        }
    }
    
    func didTappedMainMenu(_ model: MenuModel) {
        presenter.openMenu(model)
    }
    func didTappedSubMenu(_ model: MenuModel) {
        presenter.openMenu(model)
    }
}

// MARK: - CustomNavigationControllerDelegate
extension MenuViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedBarButton(type)
    }
}
