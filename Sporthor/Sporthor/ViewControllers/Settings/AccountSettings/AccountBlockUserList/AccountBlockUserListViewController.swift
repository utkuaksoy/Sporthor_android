//
//  AccountBlockUserListViewController.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import UIKit
import BarVisibilityKit

final class AccountBlockUserListViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: AccountBlockUserListPresenterProtocol {
        get { return self.basePresenter as! AccountBlockUserListPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .plain)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = UIEdgeInsets(top: 16, left: 0, bottom: 16, right: 0)
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
        tableView.removeEmptyCell()
        return tableView
    }()
    
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
        configureTabBarVisibility(at: .willAppear(isHidden: true))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        setUpNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: true))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        configureTabBarVisibility(at: .willDisappear)
        configureNavigationBarVisibility(at: .willDisappear)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        configureTabBarVisibility(at: .didDisappear)
    }
    
    // MARK: - Custom Methods
    
}

// MARK: - AccountSettingsPresenterDelegate
extension AccountBlockUserListViewController: AccountBlockUserListPresenterDelegate {
    func prepareUI() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
    
    func setUpNavigationBar() {
        guard !isNavigationAndTabbarHidden else {
            navigationController?.setNavigationBarHidden(isNavigationAndTabbarHidden, animated: false)
            return
        }
        configureNavigationBar()
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        
        appearance.titleTextAttributes = [
            .foregroundColor: DesignKitColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

extension AccountBlockUserListViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.users.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let user = presenter.users[indexPath.row]
        let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
        cell.bind(
            name: user.name,
            role: user.username,
            imageUrl: user.imageUrl,
            isGroupSelection: true,
            isSelected: user.isSelected
        )
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

extension AccountBlockUserListViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
