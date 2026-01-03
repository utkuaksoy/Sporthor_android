//
//  NotificationViewController.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import UIKit
import BarVisibilityKit

final class NotificationViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: NotificationPresenterProtocol {
        get { return self.basePresenter as! NotificationPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 12, right: 0)
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.removeEmptyCell()
        return tableView
    }()
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    // MARK: - UI Elements
    
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
        setupNavigationBar()
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
    
    @objc private func didTappedBackButton() {
        presenter.didTappedNavigationButton(.back)
    }
    
    func setupNavigationBar() {
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
        appearance.shadowColor = .clear
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.leftBarButtonItem = backButton
    }
}

// MARK: - NotificationPresenterDelegate
extension NotificationViewController: NotificationPresenterDelegate {
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

// MARK: - UITableViewDataSource & UITableViewDelegate
extension NotificationViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return presenter.filteredNotifications.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = HorizontalUsernameTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      model: presenter.headerTabs,
                      type: .notification,
                      leadingCons: 16)
            return cell
        case 1:
            let notification = presenter.filteredNotifications[indexPath.row]
            switch notification.notificationType {
            case .confirm:
                switch notification.pushMessageType {
                case .trainingGroupRequest:
                    let cell = NotificationTrainingGroupRequestTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self, model: notification)
                    return cell
                case .followRequest:
                    let cell = NotificationFollowRequestTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self, model: notification)
                    return cell
                default:
                    return UITableViewCell()
                }
            case .notification:
                switch notification.pushMessageType {
                case .chat:
                    let cell = NotificationChatTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self, model: notification)
                    return cell
                case .follow:
                    let cell = NotificationFollowInfoTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self,
                                   model: notification)
                    return cell
                case .like:
                    let cell = NotificationLikeTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(model: notification)
                    return cell
                case .newPost:
                    let cell = NotificationNewPostTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self,
                                   model: notification)
                    return cell
                case .trainingGroupRequest:
                    let cell = NotificationTrainingGroupRequestInfoTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self, model: notification)
                    return cell
                case .newTask:
                    let cell = NotificationTrainingGroupRequestInfoTableViewCell.dequeue(from: tableView, at: indexPath)
                    cell.configure(delegate: self, model: notification)
                    return cell
                default:
                    return UITableViewCell()
                }
            }
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate
extension NotificationViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 34
        default:
            return UITableView.automaticDimension
        }
    }
}

// MARK: - HorizontalUsernameTableViewCellDelegate
extension NotificationViewController: HorizontalUsernameTableViewCellDelegate {
    func didSelectItem(model: Any) {
        presenter.didSelectHeaderTab(model: model as! NotificationHeaderModel)
    }
}

// MARK: - NotificationFollowInfoTableViewCellDelegate
extension NotificationViewController: NotificationFollowInfoTableViewCellDelegate {
    func didTappedProfileNotificationFollowInfo(model: NotificationModel) {
        presenter.openProfile(model: model)
    }
}

// MARK: - NotificationNewPostTableViewCellDelegate
extension NotificationViewController: NotificationNewPostTableViewCellDelegate {
    func didTappedProfileNotificationNewPost(model: NotificationModel) {
        presenter.openProfile(model: model)
    }
}

// MARK: - NotificationChatTableViewCellDelegate
extension NotificationViewController: NotificationChatTableViewCellDelegate {
    func didTappedProfileNotificationChat(model: NotificationModel) {
        presenter.openProfile(model: model)
    }
    
    func didTappedOpenChat(model: NotificationModel) {
        presenter.openChat(model: model)
    }
}

extension NotificationViewController: NotificationTrainingGroupRequestTableViewCellDelegate {
    func didTappedTrainingGroupRequestButton(_ model: NotificationModel, isAccepted: Bool) {
        presenter.didTappedTrainingGroupRequestButton(model, isAccepted: isAccepted)
    }
}

extension NotificationViewController: NotificationTrainingGroupRequestInfoTableViewCellDelegate {
    func didTappedImageTrainingGroupRequestInfoInfo(model: NotificationModel) {}
}

extension NotificationViewController: NotificationNewTaskTableViewCellDelegate {
    func didTappedImageNewTask(model: NotificationModel) {}
}

extension NotificationViewController: NotificationFollowRequestTableViewCellDelegate {
    func didTappedFollowRequestButton(_ model: NotificationModel, isAccepted: Bool) {
        presenter.didTappedFollowRequestButton(model, isAccepted: isAccepted)
    }
}
