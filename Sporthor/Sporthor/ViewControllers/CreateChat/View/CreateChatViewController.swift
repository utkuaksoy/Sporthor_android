//
//  CreateChatViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import BarVisibilityKit
import ComponentKit
import DesignKit
import UIKit

final class CreateChatViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: CreateChatPresenterProtocol {
        get { return self.basePresenter as! CreateChatPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWhite0.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: .zero
        )
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([searchContainerView, indicatorView, tableView, emptyView])
        return stackView
    }()
    
    private lazy var searchContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWhite0.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(
            delegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Kime: Ara",
            placeholderColor: DesignKitColorName.contentSoft600.color,
            backgroundColor: .clear,
            cornerRadius: .zero,
            borderWidth: .zero,
            borderColor: .clear,
            selectedBorderColor: .clear,
            font: .body04Compact,
            image: Asset.searchbarSearch.image,
            clearImage: Asset.searchbarClose.image,
            cancelButtonTitle: "İptal",
            cancelButtonTitleColor: DesignKitColorName.contentStrong900.color,
            cancelButtonFont: .body04Compact,
            isHiddenCancelButton: true
        )
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        return searchBar
    }()
    
    private lazy var indicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var tableView: UITableView = {
        let tv = UITableView(frame: .zero)
        tv.delegate = self
        tv.dataSource = self
        tv.separatorStyle = .none
        tv.translatesAutoresizingMaskIntoConstraints = false
        tv.backgroundColor = ColorName.backgroundWhite0.color
        tv.register(ActionCell.self, forCellReuseIdentifier: ActionCell.reuseIdentifier)
        tv.register(ChatUserCell.self, forCellReuseIdentifier: ChatUserCell.reuseIdentifier)
        tv.translatesAutoresizingMaskIntoConstraints = false
        return tv
    }()
    
    private lazy var emptyView: EmptyMessageView = {
        let view = EmptyMessageView()
        view.backgroundColor = .white
        view.isHidden = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        setupConstraints()
        configureUI()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBar()
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
    
    private func configureUI() {
        configureNavigationBar()
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white

        appearance.titleTextAttributes = [
            .foregroundColor: ColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]

        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.title = "Yeni Sohbet"
        navigationItem.leftBarButtonItem = backButton
    }

    private func updateEmptyViewVisibility() {
        let isEmpty = presenter.isSearchActive && presenter.hasNoSearchResults
        
        emptyView.isHidden = !isEmpty
        tableView.isHidden = isEmpty
        
        emptyView.configure(with: "Sonuç Bulunamadı")
    }
    
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
}

// MARK: - CreateChatPresenterDelegate

extension CreateChatViewController: CreateChatPresenterDelegate {
    func reloadData() {
        tableView.reloadData()
        updateEmptyViewVisibility()
    }
}

// MARK: - Setup

private extension CreateChatViewController {
    func setupViews() {
        view.addSubview(containerView)
        containerView.addSubview(containerStackView)
        searchContainerView.addSubview(searchBar)
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
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            searchContainerView.heightAnchor.constraint(equalToConstant: 48),
            searchBar.leadingAnchor.constraint(equalTo: searchContainerView.leadingAnchor),
            searchBar.trailingAnchor.constraint(equalTo: searchContainerView.trailingAnchor, constant: -16),
            searchBar.topAnchor.constraint(equalTo: searchContainerView.topAnchor),
            searchBar.bottomAnchor.constraint(equalTo: searchContainerView.bottomAnchor),
            
            indicatorView.heightAnchor.constraint(equalToConstant: 1),
        ])
    }
}

extension CreateChatViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.search(text: text)
    }
}

extension CreateChatViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return presenter.numberOfSections()
    }
    
    func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        presenter.numberOfRows(in: section)
    }
    
    func tableView(
        _ tableView: UITableView,
        cellForRowAt indexPath: IndexPath
    ) -> UITableViewCell {
        presenter.cellForRow(at: indexPath, in: tableView)
    }
    
    func tableView(
        _ tableView: UITableView,
        heightForRowAt indexPath: IndexPath
    ) -> CGFloat {
        presenter.heightForRowAt(at: indexPath, in: tableView)
    }
    
    
    func tableView(
        _ tableView: UITableView,
        heightForHeaderInSection section: Int
    ) -> CGFloat {
        presenter.heightForHeaderInSection(tableView, heightForHeaderInSection: section)
    }

    func tableView(
        _ tableView: UITableView,
        heightForFooterInSection section: Int
    ) -> CGFloat {
        return 0.01
    }
    
    func tableView(
        _ tableView: UITableView,
        titleForHeaderInSection section: Int
    ) -> String? {
        presenter.titleForHeaderInSection(tableView, heightForHeaderInSection: section)
    }
    
    func tableView(
        _ tableView: UITableView,
        didSelectRowAt indexPath: IndexPath
    ) {
        presenter.didSelect(indexPath: indexPath)
    }
}
