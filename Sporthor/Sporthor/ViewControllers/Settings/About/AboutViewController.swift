//
//  AboutViewController.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import UIKit
import ComponentKit
import BarVisibilityKit

final class AboutViewController: BaseViewController, NavigationBarVisibility, TabBarVisibility {
    // MARK: - VIPER Variables
    var presenter: AboutPresenterProtocol {
        get { return self.basePresenter as! AboutPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 16, right: 0)
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.separatorStyle = .none
        tableView.removeEmptyCell()
        return tableView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
        configureNavigationBar()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
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
            navCon.isBackChevronLeft = true
        }
    }
    
    // MARK: - Custom Methods
}

// MARK: - AboutPresenterDelegate
extension AboutViewController: AboutPresenterDelegate {
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

extension AboutViewController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = ImageViewTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(UIImage(named: "about-sporthor") ?? UIImage())
            return cell
        case 1:
            let cell = TitleTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(title: "Sporthor: Sporcular ve Antrenörler İçin Yeni Nesil Sosyal Platform",
                      titleColor: DesignKitColorName.contentStrong900.color,
                      titleFont: .bold03Compact, topCons: 21,
                      bottomCons: 0)
            return cell
        case 2:
            let cell = TitleTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(title: "Spor sadece sahada değil, her anında bir takım ruhu ve disiplin gerektirir. Sporthor, sporcular ve antrenörler için özel olarak tasarlanmış bir sosyal medya platformudur. Burada sporcular; takım arkadaşları ve antrenörleriyle bağlantıda kalırken, performanslarını geliştirmek için görevler alır ve antrenman takvimlerini yönetir. Sporcular için: Sporthor, futbol, basketbol, voleybol gibi farklı branşlardaki sporcuların hem kulüpleri hem de arkadaşlarıyla etkileşim kurmasını sağlar. Sporcular antrenmanlarını planlar, başarılarını paylaşır ve takım ruhunu her an hisseder. Antrenörler için: Sporthor, antrenörlerin sporcularına görevler atamasını, takvimler oluşturmasını ve gelişim süreçlerini yakından takip etmesini kolaylaştırır. Takım içi iletişimi güçlendirerek antrenmanları daha verimli hale getirir. Sporu dijital dünyayla buluşturan Sporthor, hem bireysel gelişimi hem de takım başarısını destekleyen yeni nesil bir deneyim sunuyor.",
                      titleColor: DesignKitColorName.contentSub800.color,
                      titleFont: .body04Compact,
                      topCons: 12,
                      trailingCons: 0)
            return cell
        default:
            return UITableViewCell()
        }
    }
}

extension AboutViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
