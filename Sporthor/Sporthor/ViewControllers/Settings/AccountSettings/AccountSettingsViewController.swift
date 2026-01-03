//
//  AccountSettingsViewController.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import UIKit
import BarVisibilityKit

final class AccountSettingsViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: AccountSettingsPresenterProtocol {
        get { return self.basePresenter as! AccountSettingsPresenterProtocol }
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
extension AccountSettingsViewController: AccountSettingsPresenterDelegate {
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

extension AccountSettingsViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            let cell = InfoSwitchTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self,
                           info: "Gizli Hesap",
                           subtitle: "Hesabın herkese açık olduğunda, profilini ve gönderilerini Sporthor hesapları olmasa bile Sporthor’da veya Sporthor dışında herkes görebilir.",
                           isOn: presenter.isPrivateAccount,
                           isSeparator: true,
                           alignment: .top,
                           tag: 0)
            return cell
        case 1:
            let cell = InfoSwitchTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self,
                           info: "Hesabı Sil",
                           isOn: presenter.isDeleteAccount,
                           isSeparator: false,
                           tag: 1)
            return cell
        case 2:
            let cell = MenuTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self, title: "Engellenen Kullanıcılar", tag: 2)
            return cell
        default:
            return UITableViewCell()
        }
    }
}

extension AccountSettingsViewController: InfoSwitchTableViewCellDelegate {
    func didChangeSwitch(isOn: Bool, tag: Int) {
        presenter.didChangeSwitch(isOn: isOn, tag: tag)
    }
}

extension AccountSettingsViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}

extension AccountSettingsViewController: MenuTableViewCellDelegate {
    func didTappedMenu(tag: Int) {
        presenter.didSelectRowAt(tag)
    }
}
