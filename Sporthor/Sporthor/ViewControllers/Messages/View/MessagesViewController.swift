//
//  MessagesViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import BarVisibilityKit
import DesignKit
import UIKit

final class MessagesViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    
    // MARK: - VIPER Variables
    var presenter: MessagesPresenterProtocol {
        get { return self.basePresenter as! MessagesPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
        
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(
            arrangedSubviews: [
                headerView,
                tableView,
                emptyView
            ]
        )
        stackView.axis = .vertical
        stackView.distribution = .fill
        stackView.alignment = .fill
        stackView.spacing = 8
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var headerView: MessagesHeaderView = {
        let view = MessagesHeaderView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.allowsSelection = true
        tableView.alwaysBounceVertical = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.removeEmptyCell()
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 20, right: 0)
        tableView.backgroundColor = .clear
        tableView.showsVerticalScrollIndicator = false
        return tableView
    }()
    
    private lazy var emptyView: EmptyMessageView = {
        let view = EmptyMessageView()
        view.backgroundColor = .white
        view.isHidden = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Private Properties

    private var headerHeightConstraint: NSLayoutConstraint!
    private var isHeaderHidden: Bool = false
    private var searchWorkItem: DispatchWorkItem?
    private var lastContentOffset: CGFloat = 0
    private let scrollThreshold: CGFloat = 120
        
    private var isNavigationAndTabbarHidden: Bool {
        return navigationController?.viewControllers.count == 1
    }
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
        setupViews()
        setupConstraints()
        setupUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBarVisibility(at: .willAppear(isHidden: isNavigationAndTabbarHidden))
        presenter.viewWillAppear()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: false))
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
    
    private func shouldHideHeaderView(_ shouldHide: Bool) {
        let targetHeight: CGFloat = shouldHide ? 0 : 106
        let targetAlpha: CGFloat = shouldHide ? 0 : 1

        guard headerHeightConstraint.constant != targetHeight else { return }

        UIView.animate(withDuration: 0.3, delay: 0, options: [.curveEaseInOut], animations: {
            self.headerHeightConstraint.constant = targetHeight
            self.headerView.alpha = targetAlpha
            self.view.layoutIfNeeded()
        })
    }
    
    private func updateEmptyViewVisibility() {
        let isEmpty = presenter.messages?.isEmpty ?? true
        
        emptyView.isHidden = !isEmpty
        tableView.isHidden = isEmpty
        
        if isEmpty {
            if presenter.hasNoSearchResults {
                emptyView.configure(with: "Sonuç Bulunamadı")
            } else {
                emptyView.configure(with: "Henüz hiç mesajınız yok")
            }
        }
    }
}

// MARK: - MessagesPresenterDelegate

extension MessagesViewController: MessagesPresenterDelegate {
    
    func configureHeaderView() {
        headerView.configure(delegate: presenter)
    }
    
    func reloadData() {
        tableView.reloadData()
        updateEmptyViewVisibility()
    }
    
    func reloadRow(for indexPath: IndexPath) {
        DispatchQueue.main.async { [weak self] in
            self?.tableView.performBatchUpdates {
                self?.tableView.reloadRows(at: [indexPath], with: .none)
            }
        }
    }
    
    func removeRow(for indexPath: IndexPath) {
        tableView.performBatchUpdates {
            tableView.deleteRows(at: [indexPath], with: .automatic)
        } completion: { _ in
            if self.tableView.contentSize.height <= self.tableView.frame.height {
                self.shouldHideHeaderView(false)
                self.isHeaderHidden = false
            }
            self.updateEmptyViewVisibility()
        }
    }
}

// MARK: - Setup

private extension MessagesViewController {
    func setupUI() {
        setUpNavigationBar()
        setupHeaderView()
        setupCollectionView()
    }
    
    func setUpNavigationBar() {
        navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    func setupCollectionView() {
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    func setupHeaderView() {
        headerHeightConstraint = headerView.heightAnchor.constraint(equalToConstant: 106)
        headerHeightConstraint.isActive = true
        configureHeaderView()
    }
    
    func setupViews() {
        view.addSubview(containerView)
        containerView.addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])
    }
}

extension MessagesViewController: UITableViewDataSource {
    
    func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        return presenter.messages?.count ?? .zero
    }
    
    func tableView(
        _ tableView: UITableView,
        cellForRowAt indexPath: IndexPath
    ) -> UITableViewCell {
        let cell = MessageItemCell.dequeue(from: tableView, at: indexPath)
        let item = presenter.getItem(with: indexPath)
        cell.selectionStyle = .none
        cell.configure(item: item)
        return cell
    }
}

extension MessagesViewController: UITableViewDelegate {
    
    func tableView(
        _ tableView: UITableView,
        heightForRowAt indexPath: IndexPath
    ) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(
        _ tableView: UITableView,
        didSelectRowAt indexPath: IndexPath
    ) {
        guard let item = presenter.getItem(with: indexPath) else { return }
        
        presenter.markMessageAsRead(for: indexPath)
        presenter.didSelectMessage(
            userId: item.userId,
            displayName: item.userName,
            image: item.image,
            isGroup: item.isGroup,
            toUserId: item.toUserId
        )
    }
    
    func scrollViewWillEndDragging(
        _ scrollView: UIScrollView,
        withVelocity velocity: CGPoint,
        targetContentOffset: UnsafeMutablePointer<CGPoint>
    ) {
        let currentOffset = scrollView.contentOffset.y
        let delta = currentOffset - lastContentOffset
        
        guard abs(delta) > scrollThreshold else { return }
        
        let shouldHide = delta > 0
        guard shouldHide != isHeaderHidden else { return }
        
        isHeaderHidden = shouldHide
        shouldHideHeaderView(shouldHide)
        
        lastContentOffset = currentOffset
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        lastContentOffset = scrollView.contentOffset.y
    }
    
    func tableView(
        _ tableView: UITableView,
        trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath
    ) -> UISwipeActionsConfiguration? {
        let deleteAction = UIContextualAction(style: .destructive, title: nil) { [weak self] (_, _, completion) in
            guard let self = self, let item = presenter.getItem(with: indexPath) else { return }
            presenter.didTapDelete(item: item, indexPath: indexPath)
            completion(true)
        }
        deleteAction.backgroundColor = ColorName.errorBase500.color
        deleteAction.image = .trashIcon
        
//        let muteAction = UIContextualAction(style: .normal, title: nil) { [weak self] (_, _, completion) in
//            guard let self = self, let item = presenter.getItem(with: indexPath) else { return }
//            presenter.didTapMute(item: item, indexPath: indexPath)
//            completion(true)
//        }
//        muteAction.backgroundColor = ColorName.backgroundWeak100.color
//        muteAction.image = .bellSlashIcon
        
        let config = UISwipeActionsConfiguration(actions: [deleteAction])
        config.performsFirstActionWithFullSwipe = false
        return config
    }
}

// MARK: - MessagesHeaderViewDelegate
extension MessagesViewController: MessagesHeaderViewDelegate {
    func didTapAddButton() {
        presenter.didTapAddButton()
    }
    
    func didSearchTextChange(_ text: String) {
        searchWorkItem?.cancel()
        let workItem = DispatchWorkItem { [weak self] in
            self?.presenter.didSearchTextChange(text)
        }
        searchWorkItem = workItem
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: workItem)
    }
}
